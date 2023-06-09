<?php
// DBConfig.php의 내용으로 DB 접속
require_once('DBConfig.php');

mysqli_set_charset($con,"utf8");

$arr = array();
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $user_id = $_POST['user_id'];
    $query = "DELETE FROM record_dictionary WHERE user_id = '$user_id'";
    mysqli_query($con, $query);

    $query = "DELETE FROM user WHERE user_id = '$user_id'";

    if (mysqli_query($con, $query)) {
        array_push($arr, array("success" => "1")); // 회원 삭제 성공
    } else {
        array_push($arr, array("success" => "-1",
                               "error" => mysqli_error($con))); // 오류 메시지 추가
    }
} else {
    array_push($arr, array("success" => "error"));
}
echo json_encode($arr, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE);
// DB 접속 해제
mysqli_close($con);
?>
