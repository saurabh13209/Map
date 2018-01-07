<?php
    define('HOST','localhost');
	define('USER','id4030086_saurabh');
	define('PASSWORD','sunshine');
	define('DB','id4030086_account');
	
	$con = mysqli_connect(HOST,USER,PASSWORD,DB) or die;
	
	
	$name = $_POST['name'];
	
	$sql = "select lat , lng from account  where name = '$name' ; ";
	$result = mysqli_query($con , $sql);
	$row = mysqli_fetch_assoc($result);
	$array = array();
	$array[0] = $row['lat'];
	$array[1] = $row['lng'];
	echo json_encode($array);
	mysqli_close($con);
?>

