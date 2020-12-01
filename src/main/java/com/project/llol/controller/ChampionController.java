package com.project.llol.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.llol.dto.ChampionDTO;
import com.project.llol.dto.ChampionImageDTO;
import com.project.llol.dto.ChampionPassiveDTO;
import com.project.llol.dto.ChampionSpellsDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Controller
public class ChampionController {
    @RequestMapping(value = "/championList", method = RequestMethod.GET)
    public String championList(Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "championInfo", required = false, defaultValue = "") String championInfo) {
        // champions에 json 파일의 string 값을 가져옴
        BufferedReader in = null;
        String champions = null;
        try {
            URL requestUrl_championList_kr = new URL("http://ddragon.leagueoflegends.com/cdn/10.24.1/data/ko_KR/champion.json");
            HttpURLConnection con_championList_kr = (HttpURLConnection)requestUrl_championList_kr.openConnection();

            // 챔피언 key값을 가지고 있는 데이터 스트링 확보
            in = new BufferedReader(new InputStreamReader(con_championList_kr.getInputStream()));
            champions = in.readLine();

            con_championList_kr.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        // champions 스트링을 json 오브젝트로 변환함
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject)jsonParser.parse(champions);
            jsonObject = (JSONObject)jsonObject.get("data");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 키의 이름을 모르기 때문에 키셋을 가져와서 iterator를 통해 반복문으로 하나씩 가져온다.
        Set keySet = jsonObject.keySet();
        Iterator i = keySet.iterator();
        List<ChampionDTO> championList = new ArrayList<ChampionDTO>();
        while(i.hasNext()) {
            JSONObject jObject = new JSONObject();
            jObject = (JSONObject)jsonObject.get(i.next());

            // championDTO에 챔피언 데이터를 하나하나 저장한다.
            ObjectMapper mapper = new ObjectMapper();
            ChampionDTO champion = null;
            try {
                // DTO 클래스에 맞게 jsonobject를 객체로 매핑한다.
                champion = mapper.readValue(jObject.toString(), ChampionDTO.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if(champion != null) {
                // 검색어에 공백으로 검색했다면 그냥 영웅 데이터를 리스트에 포함한다.
                if(name.equals("")) {
                    championList.add(champion);
                } else {
                    // 챔피언 이름을 검색했을 경우
                    // 현재 champion 객체에 저장된 name 값에 검색어가 포함되어 있는지 확인한다.
                    if(champion.getName().indexOf(name) != -1) {
                        championList.add(champion);
                    }
                }
                // 이미지를 클릭한 챔피언 정보를 반환
                if(championInfo.equals(champion.getName())) {
                    // champions에 json 파일의 string 값을 가져옴
                    in = null;
                    String str_championDetail = null;
                    try {
                        URL request_championDetail = new URL("http://ddragon.leagueoflegends.com/cdn/10.24.1/data/ko_KR/champion/" + champion.getId() + ".json");
                        HttpURLConnection con_championDetail = (HttpURLConnection)request_championDetail.openConnection();

                        // 챔피언 key값을 가지고 있는 데이터 스트링 확보
                        in = new BufferedReader(new InputStreamReader(con_championDetail.getInputStream()));
                        str_championDetail = in.readLine();

                        con_championDetail.disconnect();
                    } catch(Exception e) {
                        e.printStackTrace();
                    } finally {
                        if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
                    }

                    // 챔피언 상세 정보
                    List<ChampionSpellsDTO> spell_list = null;
                    ChampionPassiveDTO passive = null;
                    try {
                        JSONObject championDetail = (JSONObject)jsonParser.parse(str_championDetail);
                        championDetail = (JSONObject)championDetail.get("data");
                        championDetail = (JSONObject)championDetail.get(champion.getId());

                        JSONArray spells = (JSONArray)championDetail.get("spells");
                        JSONObject json_passive = (JSONObject)championDetail.get("passive");

                        passive = new ChampionPassiveDTO();
                        try{
                            passive.setImage(mapper.readValue(json_passive.get("image").toString(), ChampionImageDTO.class));
                            passive.setName(json_passive.get("name").toString());
                            passive.setDescription(json_passive.get("description").toString());
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        Iterator it = spells.iterator();
                        spell_list = new ArrayList<ChampionSpellsDTO>();
                        ChampionSpellsDTO championSpell = null;
                        while(it.hasNext()) {
                            JSONObject spell = (JSONObject)it.next();
                            championSpell = new ChampionSpellsDTO();
                            try {
                                championSpell.setImage(mapper.readValue(spell.get("image").toString(), ChampionImageDTO.class));
                                championSpell.setName(spell.get("name").toString());
                                championSpell.setDescription(spell.get("description").toString());
                                championSpell.setCooldownBurn(spell.get("cooldownBurn").toString());
                                championSpell.setCostBurn(spell.get("costBurn").toString());
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            spell_list.add(championSpell);
                        }

                        // 챔피언 데이터에 있는 lore 칼럼이 스토리 전문이기 때문에 내용을 변경해줌
                        champion.setBlurb(championDetail.get("lore").toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    model.addAttribute("championInfo", champion);
                    model.addAttribute("passive", passive);
                    model.addAttribute("spell_list", spell_list);
                    // model.addAttribute("championSpell", championSpell);
                }
            }
        }
        if(championList.size() == 0) {
            championList = null;
        } else {
            championList.sort(new Comparator<ChampionDTO>() {
                @Override
                public int compare(ChampionDTO o1, ChampionDTO o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        model.addAttribute("championList", championList);
        return "championList";
    }
}