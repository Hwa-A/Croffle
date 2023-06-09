<?php
    // Post 방식으로 index 값 받은 경우
    if($_SERVER["REQUEST_METHOD"]=="POST"){

        // 쿼리 실행 결과를 담을 배열 선언 및 초기화
        $result = array();

        // DBConfig.php의 내용으로 DB 접속
        require_once('DBConfig.php');

        mysqli_set_charset($con,"utf8");

        // POST로 보내진 값을 받아 변수에 입력
        $id = $_POST["id"];     
        $termEng = $_POST["termEng"];     
        $termKor = $_POST["termKor"];         
        $termExplain = $_POST["termExplain"];
    

        // 쿼리문을 담을 변수 선언 및 초기화
        $sql="";

        // 해당 사용자의 단어장 데이터 select 쿼리문
        $sql = "INSERT INTO record_dictionary
                VALUES ('$id', '$termEng', '$termKor', '$termExplain')";
    
        $res = mysqli_query($con, $sql) or die(false);
            
        if ($res){   
            // 성공적으로 데이터 추가한 경우
            array_push($result, array("success"=>"success"));   
        }else{    
            // 실패한 경우
            array_push($result, array("success"=>"fail"));    
        }
        
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