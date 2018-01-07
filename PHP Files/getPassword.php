<?php
    define('HOST','localhost');
	define('USER','id4030086_saurabh');
	define('PASSWORD','sunshine');
	define('DB','id4030086_account');
	
	$con = mysqli_connect(HOST,USER,PASSWORD,DB) or die;

	$Name = $_POST['Name'];
	
	$sql = "select password from account where name = '$Name'";
	$result = mysqli_query($con , $sql);
	$row = mysqli_fetch_assoc($result);
	
	echo json_encode(array($row['password']));
	
	mysqli_close($con);
?>

