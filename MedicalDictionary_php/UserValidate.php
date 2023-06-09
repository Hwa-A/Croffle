<?php
  // DBConfig.php의 내용으로 DB 접속
  require_once('DBConfig.php');

  mysqli_set_charset($con,"utf8");

  $user_id = $_POST["user_id"];
  $statement = mysqli_prepare($con, "SELECT user_id FROM user WHERE user_id = ?");

  mysqli_stmt_bind_param($statement, "s", $user_id);
  mysqli_stmt_execute($statement);
  mysqli_stmt_store_result($statement);//결과를 클라이언트에 저장함
  mysqli_stmt_bind_result($statement, $user_id);//결과를 $user_id에 바인딩함

  $response = array();
  $response["success"] = true;

  while(mysqli_stmt_fetch($statement)){
    $response["success"] = false;//회원가입불가를 나타냄
    $response["user_id"] = $user_id;
  }

  //데이터베이스 작업이 성공 혹은 실패한것을 알려줌
  echo json_encode($response, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE);
  // DB 접속 해제
  mysqli_close($con);
?>