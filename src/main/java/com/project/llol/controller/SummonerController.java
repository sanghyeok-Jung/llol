package com.project.llol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.llol.dto.ChampionMasteryDTO;
import com.project.llol.dto.LeagueEntryDTO;
import com.project.llol.dto.SummonerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class SummonerController {
    @Value("${RIOT_API_KEY}")
    private String API_KEY;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainView() {
        return "main";
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String findUser(Model model, @RequestParam(value = "name", defaultValue = "hide on bush") String name) {
        // 입력받은 name에 공백이 있을 경우, %20으로 대체 함 (라이엇에서 공백을 %20으로 처리)
        String summonerName = name.replaceAll(" ", "%20");

        BufferedReader in = null;
        SummonerDTO summoner = null;

        // 이름 검색으로 유저 정보를 가져오는 부분
        try {
            // 입력된 문자열을 URL 객체로 변환 (유저 정보 조회)
            URL requestUrl = new URL("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=" + API_KEY);
            HttpURLConnection con = (HttpURLConnection)requestUrl.openConnection();

            // 라이엇 API에서 검색에 성공했을 때 전달되는 responseCode는 200임
            if(con.getResponseCode() == 200) {
                // 연결된 객체에서 데이터를 가져오고 BufferedReader에 저장함
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 닉네임 검색에 실패했을 경우 예외 처리
                con.disconnect();
                return "common/error";
            }

            // 가져온 데이터를 문자열 데이터에 저장함
            String jsonText = in.readLine();

            // json 객체로 매핑해줄 ObjectMapper 클래스의 객체 생성
            ObjectMapper mapper = new ObjectMapper();
            // Mapper 클래스의 readValue 메소드로 문자열을 SummonerDTO 클래스에 맞게 변환하고
            // SummonerDTO 클래스의 객체 summoner 에 데이터 저장
            summoner = mapper.readValue(jsonText, SummonerDTO.class);

            // 연결 해제
            con.disconnect();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        // ----------------------------------------------------------------
        // 가져온 유저 정보를 바탕으로 ID를 추출하고, 티어 및 리그 전적을 조회하는 부분
        LeagueEntryDTO[] leagueEntry = null;
        LeagueEntryDTO soloLeagueEntry = null;
        LeagueEntryDTO flexLeagueEntry = null;
        try {
            // 리그 티어, 전적 검색
            String summonerId = summoner.getId();

            // 입력된 문자열을 URL 객체로 변환 (유저 ID 기반으로 티어 및 전적 검색)
            URL requestUrl = new URL("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + summonerId + "?api_key=" + API_KEY);
            HttpURLConnection con = (HttpURLConnection)requestUrl.openConnection();

            // 라이엇 API에서 검색에 성공했을 때 전달되는 responseCode는 200임
            if(con.getResponseCode() == 200) {
                // 연결된 객체에서 데이터를 가져오고 BufferedReader에 저장함
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 닉네임 검색에 실패했을 경우 예외 처리
                con.disconnect();
                return "common/error";
            }

            // 가져온 데이터를 문자열 데이터에 저장함
            String jsonText = in.readLine();

            // json 객체로 매핑해줄 ObjectMapper 클래스의 객체 생성
            ObjectMapper mapper = new ObjectMapper();
            // Mapper 클래스의 readValue 메소드로 문자열을 LeagueEntryDTO 클래스에 맞게 변환하고
            // LeagueEntryDTO 클래스의 객체 leagueEntry 에 데이터 저장
            leagueEntry = mapper.readValue(jsonText, LeagueEntryDTO[].class);

            // 리그 정보에 따라 솔로랭크, 자유랭크 객체로 따로 저장
            for(LeagueEntryDTO dto : leagueEntry) {
                if(dto.getQueueType().equals("RANKED_FLEX_SR")) {
                    flexLeagueEntry = dto;
                } else {
                    soloLeagueEntry = dto;
                }
            }

            // 연결 해제
            con.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        // ----------------------------------------------------------------
        // 사전 데이터 확보 (챔피언 목록 데이터)
        // 챔피언 key값에 일치하는 champion이름을 알아내기 위한 데이터 페이지 연결 (영문 페이지)
        BufferedReader in_championList = null;
        String champions = null;
        try {
            URL requestUrl_championList = new URL("http://ddragon.leagueoflegends.com/cdn/10.23.1/data/en_US/champion.json");
            HttpURLConnection con_championList = (HttpURLConnection)requestUrl_championList.openConnection();

            // 챔피언 key값을 가지고 있는 데이터 스트링 확보
            in_championList = new BufferedReader(new InputStreamReader(con_championList.getInputStream()));
            champions = in_championList.readLine();

            con_championList.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in_championList != null) try { in_championList.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        // 챔피언 key값에 일치하는 champion이름을 알아내기 위한 데이터 페이지 연결 (한글 페이지)
        // 챔피언 이름을 한글로 보여주기 위함
        BufferedReader in_championList_kr = null;
        String champions_kr = null;
        try {
            URL requestUrl_championList_kr = new URL("http://ddragon.leagueoflegends.com/cdn/10.23.1/data/ko_KR/champion.json");
            HttpURLConnection con_championList_kr = (HttpURLConnection)requestUrl_championList_kr.openConnection();

            // 챔피언 key값을 가지고 있는 데이터 스트링 확보
            in_championList_kr = new BufferedReader(new InputStreamReader(con_championList_kr.getInputStream()));
            champions_kr = in_championList_kr.readLine();

            con_championList_kr.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in_championList_kr != null) try { in_championList_kr.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        // 챔피언 숙련도를 조회하는 부분
        ChampionMasteryDTO[] championList = null;
        ChampionMasteryDTO top1 = null;
        String top1_name = null;
        String top1_name_kr = null;
        ChampionMasteryDTO top2 = null;
        String top2_name = null;
        String top2_name_kr = null;
        ChampionMasteryDTO top3 = null;
        String top3_name = null;
        String top3_name_kr = null;

        try {
            // 리그 티어, 전적 검색
            String summonerId = summoner.getId();

            // 입력된 문자열을 URL 객체로 변환 (유저 ID 기반으로 티어 및 전적 검색)
            URL requestUrl = new URL("https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/" + summonerId + "?api_key=" + API_KEY);
            HttpURLConnection con = (HttpURLConnection)requestUrl.openConnection();

            // 라이엇 API에서 검색에 성공했을 때 전달되는 responseCode는 200임
            if(con.getResponseCode() == 200) {
                // 연결된 객체에서 데이터를 가져오고 BufferedReader에 저장함
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 닉네임 검색에 실패했을 경우 예외 처리
                con.disconnect();
                return "common/error";
            }

            // 가져온 데이터를 문자열 데이터에 저장함
            String jsonText = in.readLine();

            // json 객체로 매핑해줄 ObjectMapper 클래스의 객체 생성
            ObjectMapper mapper = new ObjectMapper();
            // Mapper 클래스의 readValue 메소드로 문자열을 ChampionMasteryDTO 클래스에 맞게 변환하고
            // ChampionMasteryDTO 클래스의 객체 championList 에 데이터 저장
            championList = mapper.readValue(jsonText, ChampionMasteryDTO[].class);

            // 연결 해제
            con.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
        }

        // 챔피언 숙련도 TOP 3 보유 여부 확인 및 객체 저장
        if(championList != null) {
            // 숙련도 보유 챔피언 개수가 3개 이하일 가능성에 대한 처리
            switch(championList.length) {
                case 1:
                    top1 = championList[0];
                    // 영문 이름 추출 과정
                    top1_name = champions.substring(champions.indexOf("\"key\":\"" + top1.getChampionId() + "\""));
                    top1_name = top1_name.substring(top1_name.indexOf(",") + 9);
                    top1_name = top1_name.substring(0, top1_name.indexOf("\""));

                    // 한글 이름 추출 과정
                    top1_name_kr = champions_kr.substring(champions_kr.indexOf("\"key\":\"" + top1.getChampionId() + "\""));
                    top1_name_kr = top1_name_kr.substring(top1_name_kr.indexOf(",") + 9);
                    top1_name_kr = top1_name_kr.substring(0, top1_name_kr.indexOf("\""));

                    // 문자열 변환 과정
                    // 1. 챔피언 이름에 공백이 있으면 공백만 없애주고 챔피언 이름을 설정한다.
                    // 2. 챔피언 이름에 공백이 없으면, 앞글자만 대문자로 바꾸고 나머지는 소문자로 바꾼다.
                    if(top1_name.contains(" ")) {
                        top1_name = top1_name.replace(" ", "");
                    } else {
                        top1_name = top1_name.substring(0, 1).toUpperCase() + top1_name.substring(1).toLowerCase();
                    }

                    top2 = null;
                    top2_name = null;

                    top3 = null;
                    top3_name = null;
                    break;
                case 2:
                    top1 = championList[0];
                    // 영문 이름 추출 과정
                    top1_name = champions.substring(champions.indexOf("\"key\":\"" + top1.getChampionId() + "\""));
                    top1_name = top1_name.substring(top1_name.indexOf(",") + 9);
                    top1_name = top1_name.substring(0, top1_name.indexOf("\""));

                    // 한글 이름 추출 과정
                    top1_name_kr = champions_kr.substring(champions_kr.indexOf("\"key\":\"" + top1.getChampionId() + "\""));
                    top1_name_kr = top1_name_kr.substring(top1_name_kr.indexOf(",") + 9);
                    top1_name_kr = top1_name_kr.substring(0, top1_name_kr.indexOf("\""));

                    if(top1_name.contains(" ")) {
                        top1_name = top1_name.replace(" ", "");
                    } else {
                        top1_name = top1_name.substring(0, 1).toUpperCase() + top1_name.substring(1).toLowerCase();
                    }

                    top2 = championList[1];
                    // 영문 이름 추출 과정
                    top2_name = champions.substring(champions.indexOf("\"key\":\"" + top2.getChampionId() + "\""));
                    top2_name = top2_name.substring(top2_name.indexOf(",") + 9);
                    top2_name = top2_name.substring(0, top2_name.indexOf("\""));

                    // 한글 이름 추출 과정
                    top2_name_kr = champions_kr.substring(champions_kr.indexOf("\"key\":\"" + top2.getChampionId() + "\""));
                    top2_name_kr = top2_name_kr.substring(top2_name_kr.indexOf(",") + 9);
                    top2_name_kr = top2_name_kr.substring(0, top2_name_kr.indexOf("\""));

                    if(top2_name.contains(" ")) {
                        top2_name = top2_name.replace(" ", "");
                    } else {
                        top2_name = top2_name.substring(0, 1).toUpperCase() + top2_name.substring(1).toLowerCase();
                    }

                    top3 = null;
                    top3_name = null;
                    break;
                default:
                    top1 = championList[0];
                    // 영문 이름 추출 과정
                    top1_name = champions.substring(champions.indexOf("\"key\":\"" + top1.getChampionId() + "\""));
                    top1_name = top1_name.substring(top1_name.indexOf(",") + 9);
                    top1_name = top1_name.substring(0, top1_name.indexOf("\""));

                    // 한글 이름 추출 과정
                    top1_name_kr = champions_kr.substring(champions_kr.indexOf("\"key\":\"" + top1.getChampionId() + "\""));
                    top1_name_kr = top1_name_kr.substring(top1_name_kr.indexOf(",") + 9);
                    top1_name_kr = top1_name_kr.substring(0, top1_name_kr.indexOf("\""));

                    if(top1_name.contains(" ")) {
                        top1_name = top1_name.replace(" ", "");
                    } else {
                        top1_name = top1_name.substring(0, 1).toUpperCase() + top1_name.substring(1).toLowerCase();
                    }

                    top2 = championList[1];
                    // 영문 이름 추출 과정
                    top2_name = champions.substring(champions.indexOf("\"key\":\"" + top2.getChampionId() + "\""));
                    top2_name = top2_name.substring(top2_name.indexOf(",") + 9);
                    top2_name = top2_name.substring(0, top2_name.indexOf("\""));

                    // 한글 이름 추출 과정
                    top2_name_kr = champions_kr.substring(champions_kr.indexOf("\"key\":\"" + top2.getChampionId() + "\""));
                    top2_name_kr = top2_name_kr.substring(top2_name_kr.indexOf(",") + 9);
                    top2_name_kr = top2_name_kr.substring(0, top2_name_kr.indexOf("\""));

                    if(top2_name.contains(" ")) {
                        top2_name = top2_name.replace(" ", "");
                    } else {
                        top2_name = top2_name.substring(0, 1).toUpperCase() + top2_name.substring(1).toLowerCase();
                    }

                    top3 = championList[2];
                    // 영문 이름 추출 과정
                    top3_name = champions.substring(champions.indexOf("\"key\":\"" + top3.getChampionId() + "\""));
                    top3_name = top3_name.substring(top3_name.indexOf(",") + 9);
                    top3_name = top3_name.substring(0, top3_name.indexOf("\""));

                    // 한글 이름 추출 과정
                    top3_name_kr = champions_kr.substring(champions_kr.indexOf("\"key\":\"" + top3.getChampionId() + "\""));
                    top3_name_kr = top3_name_kr.substring(top3_name_kr.indexOf(",") + 9);
                    top3_name_kr = top3_name_kr.substring(0, top3_name_kr.indexOf("\""));

                    if(top3_name.contains(" ")) {
                        top3_name = top3_name.replace(" ", "");
                    } else {
                        top3_name = top3_name.substring(0, 1).toUpperCase() + top3_name.substring(1).toLowerCase();
                    }
            }
        }

        model.addAttribute("top1", top1);
        model.addAttribute("top1_name", top1_name);
        model.addAttribute("top1_name_kr", top1_name_kr);
        model.addAttribute("top2", top2);
        model.addAttribute("top2_name", top2_name);
        model.addAttribute("top2_name_kr", top2_name_kr);
        model.addAttribute("top3", top3);
        model.addAttribute("top3_name", top3_name);
        model.addAttribute("top3_name_kr", top3_name_kr);

        model.addAttribute("summoner", summoner);
        model.addAttribute("flexLeagueEntry", flexLeagueEntry);
        model.addAttribute("soloLeagueEntry", soloLeagueEntry);
        return "user_info";
    }
}