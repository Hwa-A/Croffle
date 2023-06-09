<?php
    // Post 방식으로 index 값 받은 경우
    if($_SERVER["REQUEST_METHOD"]=="POST"){

        // 쿼리 실행 결과를 담을 배열 선언 및 초기화
        $result = array();
        $record =  array();     // 단어장 데이터를 담을 배열

       // DBConfig.php의 내용으로 DB 접속
        require_once('DBConfig.php');

        mysqli_set_charset($con,"utf8");

        // POST로 보내진 값을 받아 변수에 입력
        $id = $_POST["id"];              
        $termIndex = $_POST["termIndex"];   

        // 쿼리문을 담을 변수 선언 및 초기화
        $sql="";

        $flag = 0;       // termIndex가 한글/영어 인덱스인지 판단할 flag변수(0:한글, 1:영어)
        $reFlag  = 0;         // record에 데이터 존재 여부 판단할 flag변수(0:데이터 존재X, 1: 데이터 존재O)
        switch($termIndex){
            case "ㄱ":
                $flag = 0;
                $termIndexStart = "가";
                $termIndexEnd = "깋";
                break;
            case "ㄴ":
                $flag = 0;
                $termIndexStart = "ㄴ";
                $termIndexEnd = "닣";
                break;
            case "ㄷ":
                $flag = 0;
                $termIndexStart = "다";
                $termIndexEnd = "딯";
                break;
            case "ㄹ":
                $flag = 0;
                $termIndexStart = "라";
                $termIndexEnd = "맇";
                break;
            case "ㅁ":
                $flag = 0;
                $termIndexStart = "마";
                $termIndexEnd = "밓";
                break;
            case "ㅂ":
                $flag = 0;
                $termIndexStart = "바";
                $termIndexEnd = "빟";
                break;
            case "ㅅ":
                $flag = 0;
                $termIndexStart = "사";
                $termIndexEnd = "싷";
                break;
            case "ㅇ":
                $flag = 0;
                $termIndexStart = "아";
                $termIndexEnd = "잏";
                break;
            case "ㅈ":
                $flag = 0;
                $termIndexStart = "자";
                $termIndexEnd = "짛";
                break;
            case "ㅊ":
                $flag = 0;
                $termIndexStart = "차";
                $termIndexEnd = "칳";
                break;
            case "ㅋ":
                $flag = 0;
                $termIndexStart = "카";
                $termIndexEnd = "킿";
                break;
            case "ㅌ":    
                $flag = 0;
                $termIndexStart = "타";
                $termIndexEnd = "팋";
                break;
            case "ㅍ":
                $flag = 0;
                $termIndexStart = "파";
                $termIndexEnd = "핗";
                break;
            case "ㅎ":
                $flag = 0;
                $termIndexStart = "하";
                $termIndexEnd = "힣";
                break;
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
            case "F":
            case "G":
            case "H":
            case "I":
            case "J":
            case "K":
            case "L":
            case "M":
            case "N":
            case "O":
            case "P":
            case "Q":
            case "R":
            case "S":
            case "T":
            case "U":
            case "V":
            case "W":
            case "X":
            case "Y":
            case "Z":
                $flag = 1;
                break;
            default:
                // 이상한 인덱스 값인 경우 
                array_push($result, array("success"=>"fail"));    
                mysqli_close($con);
                die(json_encode($result, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE));
                break;
        }
        // 선택된 인덱스(A~Z, ㄱ~ㅎ)에 해당되는 데이터 select 쿼리문
        if($flag == 0){
            // 한글 인덱스인 경우
            $sql = "SELECT * FROM  dictionary 
                    WHERE dic_term_kor between '$termIndexStart' and '$termIndexEnd'
                    order by binary(dic_term_kor)";
        }else{
            // 영어 인덱스인 경우
            $sql = "SELECT * FROM  dictionary 
                    WHERE UPPER(dic_term_eng) LIKE '$termIndex%'
                    order by dic_term_eng";
        }

        $res = mysqli_query($con, $sql);
            
        if (mysqli_num_rows($res) > 0){   
            $success = array("success"=>"success");
            // 데이터가 1개 이상 있을 경우
            while($row = mysqli_fetch_assoc($res)){
                array_push($result, array("termEng"=>$row['dic_term_eng'], 
                                          "termKor"=>$row['dic_term_kor'], 
                                          "termExplain"=>$row['dic_explain'],
                                          "termRecord"=>"n"));
            }
        }else{
            $success = array("success"=>"empty");
        }

        // 개인 단어장에서 선택된 인덱스(A~Z, ㄱ~ㅎ)에 해당되는 데이터를 select 쿼리문
        if($flag == 0){
            // 한글 인덱스인 경우
            $sql = "SELECT rdic_term_eng FROM record_dictionary 
                    WHERE user_id = '$id' and (rdic_term_kor between '$termIndexStart' and '$termIndexEnd')
                    order by rdic_term_eng";
        }else{
            // 영어 인덱스인 경우
            $sql = "SELECT rdic_term_eng FROM record_dictionary 
                    WHERE user_id = '$id' and (UPPER(rdic_term_eng) LIKE '$termIndex%')
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