<?php
    // Post 방식으로 term 값 받은 경우
    if($_SERVER["REQUEST_METHOD"]=="POST"){

        // 쿼리 실행 결과를 담을 배열 선언 및 초기화
        $result = array();
        $record = array();         // 단어장 데이터를 담을 배열

        // DBConfig.php의 내용으로 DB 접속
        require_once('DBConfig.php');

        mysqli_set_charset($con,"utf8");

        // POST로 보내진 값을 받아 변수에 입력
        $id = $_POST["id"];              
        $term = $_POST["term"];   // 검색어
        $term = trim($term);      // term의 앞, 뒤 공백 제거
        $sql = "";    // 쿼리문을 담을 변수 선언 및 초기화

        $firstTerm = substr($term, 0, 1);    // 검색어의 첫 글자
        $ptn = '/^[a-zA-Z]$/';          // 영문자 검사 패턴

        // term의 첫 글자가 영어인 경우, matchResult에 1 반환 / 아닌 경우 0 반환
        $matchResult = preg_match($ptn, $firstTerm);
        $reFlag  = 0;         // record에 데이터 존재 여부 판단할 flag변수(0:데이터 존재X, 1: 데이터 존재O)
        
        // term과 동일한 데이터 select 쿼리문
        if($matchResult == 1){
            // 영어 검색어인 경우
            $sql = "SELECT * FROM  dictionary 
                    WHERE REPLACE(UPPER(dic_term_eng), ' ', '') LIKE REPLACE(UPPER('$term'), ' ', '')";
        }else{
            // 한글 검색어인 경우
            $sql = "SELECT * FROM  dictionary 
                    WHERE REPLACE(dic_term_kor, ' ', '') LIKE REPLACE('$term', ' ', '')";
        }

        $res = mysqli_query($con, $sql);
            
        // 데이터가 1개 이상 있을 경우
        if (mysqli_num_rows($res) > 0){   
            $success = array("success"=>"success");       
            while($row = mysqli_fetch_assoc($res)){
                array_push($result, array("termEng"=>$row['dic_term_eng'], 
                                          "termKor"=>$row['dic_term_kor'], 
                                          "termExplain"=>$row['dic_explain'],
                                          "termRecord"=>"n"));
            }
        }

        // term을 포함하는 데이터 select 쿼리문
        if($matchResult == 1){
            // 영어 검색어인 경우
            $sql = "SELECT * FROM  dictionary 
                    WHERE REPLACE(UPPER(dic_term_eng), ' ', '') LIKE REPLACE(UPPER('%$term%'), ' ', '')
                    order by dic_term_eng";
        }else{
            // 한글 검색어인 경우
            $sql = "SELECT * FROM  dictionary 
                    WHERE REPLACE(dic_term_kor, ' ', '') LIKE REPLACE('%$term%', ' ', '')
                    order by binary(dic_term_kor)";
        }

        $res = mysqli_query($con, $sql);

        if (mysqli_num_rows($res) > 0){   
            // 데이터가 1개 이상 있을 경우
            if(empty($result))
                $success = array("success"=>"success");    
            while($row = mysqli_fetch_assoc($res)){
                array_push($result, array("termEng"=>$row['dic_term_eng'], 
                                          "termKor"=>$row['dic_term_kor'], 
                                          "termExplain"=>$row['dic_explain'],
                                          "termRecord"=>"n"));
            }
        }else{    
            // 데이터가 없는 경우
            $success =  array("success"=>"empty");    
        }

        // 중복 데이터 존재할 경우, 중복 데이터는 하나만 남기고 제거
        $result = array_unique($result, SORT_REGULAR);
        
        // 개인 단어장에서 term을 포함하는 데이터 select 쿼리문
        if($matchResult == 1){
            // 영어 검색어인 경우
            $sql = "SELECT rdic_term_eng FROM  record_dictionary 
                    WHERE  user_id = '$id' and (REPLACE(UPPER(rdic_term_eng), ' ', '') LIKE REPLACE(UPPER('%$term%'), ' ', ''))
                    order by rdic_term_eng";
        }else{
            // 한글 검색어인 경우
            $sql = "SELECT rdic_term_eng FROM  record_dictionary 
                    WHERE user_id = '$id' and (REPLACE(rdic_term_kor, ' ', '') LIKE REPLACE('%$term%', ' ', ''))
                    order by rdic_term_eng";
        }

        $res = mysqli_query($con, $sql);

        if (mysqli_num_rows($res) > 0){   
            // 데이터가 1개 이상 있을 경우
            $reFlag = 1;
            while($row = mysqli_fetch_assoc($res)){
                array_push($record, array("termEng"=>$row['rdic_term_eng']));
            }
        }else{
            $reFlag = 0;
        }

        // 개인 단어장에서 가져온 데이터가 1개 이상 존재하는 경우
        if($reFlag == 1){
            // 단어장에 저장된 데이터는 termRecord의 n값을 y로 변경
            for($i = 0; $i < count($record); $i++){
                $index = array_search($record[$i]["termEng"], array_column($result, "termEng"));

                if($index !== NULL){
                    // 값 변경(n -> y)
                    $result[$index]["termRecord"] = "y";
                }
            }
        }
        // 실행 결과에 대한 배열을 result 배열 맨 앞 요소로 추가
        array_unshift($result, $success);

        // json형식으로 변환 후 출력
        echo json_encode($result, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE);

        // 메모리 정리
        mysqli_free_result($res);
        // DB 접속 해제
        mysqli_close($con);
    }else{
        // POST방식이 아닌 방식으로 전달받은 경우
        array_push($result, array("success"=>"fail"));           
        die(json_encode($result, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE));
    }
?>