<?php
    define('HOST','localhost');
	define('USER','id4030086_saurabh');
	define('PASSWORD','sunshine');
	define('DB','id4030086_account');
	
	$con = mysqli_connect(HOST,USER,PASSWORD,DB) or die;
	
	
	$username = $_POST['username'];
	$password = $_POST['password'];
	
	$sql = "select lat , lng from account where name = '$username' and password = '$password'";
	$result = mysqli_query($con , $sql);
	
	$num=0;
	$array = array();
	while ($row = mysqli_fetch_assoc($result)){
	    $array[0] = $row['lat'];
	    $array[1] = $row['lng'];
		$num++;
	}
	
	if ($num>0){
	    echo json_encode($array);
	}else{
	    echo json_encode(array("false"));
	}
	mysqli_close($con);
	
?>

