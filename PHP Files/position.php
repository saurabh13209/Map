<?php
    define('HOST','localhost');
	define('USER','id4030086_saurabh');
	define('PASSWORD','sunshine');
	define('DB','id4030086_account');
	
	$con = mysqli_connect(HOST,USER,PASSWORD,DB) or die;
	
	
	$name = $_POST['name'];
	$lat = $_POST['lat'];
	$lng = $_POST['lng'];
	
	$sql = "update account set lat = '$lat' , lng = '$lng' where name = '$name' ";
	mysqli_query($con , $sql);
	
?>