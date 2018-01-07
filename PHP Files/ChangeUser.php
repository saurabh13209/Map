<?php
    define('HOST','localhost');
	define('USER','id4030086_saurabh');
	define('PASSWORD','sunshine');
	define('DB','id4030086_account');
	
	$con = mysqli_connect(HOST,USER,PASSWORD,DB) or die;

	$Old = $_POST['OldName'];
	$New = $_POST['NewName'];
	$task = $_POST['task'];
	
	if ($task == "Pass"){
	    $sql = "update account set password = '$New' where name = '$Old'";
	}else{
	    $sql = "update account set name = '$New' where name = '$Old'";
	}
	mysqli_query($con , $sql);
	mysqli_close($con);
?>
