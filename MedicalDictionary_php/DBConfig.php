<?php
 // DB 정보
 define('HOST',"localhost");            // DB서버 주소
 define('USER', "root");                // DB사용자 이름
 define('PASS', "123456");              // DB사용자 비밀번호
 define('DB', "medicaldic_db");         // DB이름
 
 // DB 접속
 $con = mysqli_connect(HOST,USER,PASS,DB) or die("DB 연결 실패 : ".mysqli_connect_error());

 // DB에 table이 없는 경우, table 생성
 // 회원 table
 $sql = "CREATE TABLE IF NOT EXISTS user(
         user_id VARCHAR(30) NOT NULL,
         user_pw VARCHAR(30) NOT NULL,
         user_name VARCHAR(30) NOT NULL,
         PRIMARY KEY(user_id)
         ) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;";

 $res = mysqli_query($con, $sql);

 // 사전 table
 $sql = "CREATE TABLE IF NOT EXISTS dictionary(
         dic_term_eng VARCHAR(50) NOT NULL,
         dic_term_kor VARCHAR(50) NOT NULL,
         dic_explain VARCHAR(700) NOT NULL,
         PRIMARY KEY(dic_term_eng)
        ) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;";

 $res = mysqli_query($con, $sql);

 // 단어장 table
 $sql = "CREATE TABLE IF NOT EXISTS record_dictionary(
 	     user_id VARCHAR(30) NOT NULL,
	     rdic_term_eng VARCHAR(50) NOT NULL,
	     rdic_term_kor VARCHAR(50) NOT NULL,
	     rdic_explain VARCHAR(700) NOT NULL,
	     PRIMARY KEY(user_id, rdic_term_eng),
	     FOREIGN KEY(user_id) REFERENCES user(user_id), 
	     FOREIGN KEY(rdic_term_eng) REFERENCES dictionary(dic_term_eng)
         ) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;";

$res = mysqli_query($con, $sql);

$sql = "SELECT * FROM dictionary;";
$res = mysqli_query($con, $sql);

if (mysqli_num_rows($res) <= 0){   
    // 데이터가 없을 경우
    setlocale(LC_CTYPE, 'ko_KR.eucKR');
    $term = array();
    $fp = fopen("Medical_Dictionary.csv", "r");
    while($datas = fgetcsv($fp, 1000, ",")){  
        for($i = 0; $i <= 2; $i++){
            $data[$i] = iconv("euc-kr", "utf-8", $datas[$i]);
        }  
        $sql = "INSERT INTO dictionary (dic_term_eng, dic_term_kor, dic_explain)
                VALUES ('$data[0]', '$data[1]','$data[2]');";

        $res = mysqli_query($con, $sql);
    }
    // 파일 닫기
    fclose($fp);
}

?>