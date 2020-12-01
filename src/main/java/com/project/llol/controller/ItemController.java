package com.project.llol.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.llol.dto.ImageDTO;
import com.project.llol.dto.ItemDTO;
import com.project.llol.dto.ItemGoldDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Controller
public class ItemController {
    @RequestMapping(value = "/itemList", method = RequestMethod.GET)
    public String itemList(Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "itemInfo", required = false, defaultValue = "") String itemInfo) {
        BufferedReader in = null;
        List<ItemDTO> itemList = null;
        ItemDTO itemDetail = null;
        ItemDTO item = null;

        ObjectMapper mapper = new ObjectMapper();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        // 데이터 문서로 연결하여 jsonObject 변환
        try {
            URL requestURL = new URL("http://ddragon.leagueoflegends.com/cdn/10.24.1/data/ko_KR/item.json");
            HttpURLConnection connection = (HttpURLConnection)requestURL.openConnection();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            jsonObject = (JSONObject)jsonParser.parse(in.readLine());
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 추출한 jsonObject에서 data 키에 해당하는 값으로 접근 (실질적인 데이터 위치)
        jsonObject = (JSONObject)jsonObject.get("data");
        Set keySet = jsonObject.keySet();
        Iterator it = keySet.iterator();
        itemList = new ArrayList<ItemDTO>();
        while(it.hasNext()) {
            item = new ItemDTO();
            item.setKey(it.next().toString());

            JSONObject itemObject = (JSONObject)jsonObject.get(item.getKey());
            item.setName(itemObject.get("name").toString());
            item.setPlaintext(itemObject.get("plaintext").toString());
            item.setDescription(itemObject.get("description").toString());
            // item.setImage
            try {
                item.setImage(mapper.readValue(itemObject.get("image").toString(), ImageDTO.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // item.setGold
            try {
                item.setGold(mapper.readValue(itemObject.get("gold").toString(), ItemGoldDTO.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // item.setFrom
            if(itemObject.containsKey("from")) {
                try {
                    item.setFrom((List)itemObject.get("from"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                item.setFrom(null);
            }
            // item.setInto
            if(itemObject.containsKey("into")) {
                try {
                    item.setInto((List)itemObject.get("into"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                item.setInto(null);
            }

            // 검색 조건 확인 후 리스트에 저장
            if(name.equals("")) {
                itemList.add(item);
            } else {
                if(item.getName().indexOf(name) != -1) {
                    itemList.add(item);
                }
            }

            if(!itemInfo.equals("")) {
                if(item.getKey().equals(itemInfo)) {
                    itemDetail = item;
                }
            }
        }

        if(itemList.size() == 0) {
            itemList = null;
        } else {
            itemList.sort(new Comparator<ItemDTO>() {
                @Override
                public int compare(ItemDTO o1, ItemDTO o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("itemInfo", itemDetail);
        return "itemList";
    }
}
