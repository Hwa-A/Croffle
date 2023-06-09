<?php
// DBConfig.php의 내용으로 DB 접속
require_once('DBConfig.php');
 
$arr = array();
mysqli_set_charset($con,"utf8");

if ($_SERVER["REQUEST_METHOD"]=="POST") {

    $user_id = $_POST['user_id'];
    $user_pw = $_POST['user_pw'];
    $user_name = $_POST['user_name'];

    $query = "insert into user (user_id,user_pw,user_name) values ('$user_id','$user_pw','$user_name') ";
    
    if (mysqli_query($con, $query)) {
        array_push($arr, array("success"=>"1"));
        //$arr["success"] = "1";
    } else {
        array_push($arr, array("success"=>"-1"));
        //$arr["success"] = "-1";
    } 

echo json_encode($arr,JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE);

mysqli_close($con); 
}
else { $arr["success"] = "error"; } 

?>