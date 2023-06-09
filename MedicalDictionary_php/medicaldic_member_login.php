<?php
// DBConfig.php의 내용으로 DB 접속
require_once('DBConfig.php');

mysqli_set_charset($con,"utf8");

$arr = array();
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $user_id = $_POST['user_id'];
    $user_pw = $_POST['user_pw'];

    $query = "SELECT user_id, user_name FROM user WHERE user_id='$user_id' AND user_pw='$user_pw'";

    $result = mysqli_query($con, $query);

    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        $user_name = $row['user_name'];
        array_push($arr, array("success" => "1", "user_name" => $user_name)); // 로그인 성공 및 사용자 이름 포함
    } else {
        array_push($arr, array("success" => "-1")); // 로그인 실패
    }
} else {
    $arr["success"] = "error";
}
echo json_encode($arr, JSON_PRETTY_PRINT|JSON_UNESCAPED_UNICODE);
// DB 접속 해제
mysqli_close($con);
?>
