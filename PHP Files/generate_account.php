<?php
    define('HOST','localhost');
	define('USER','id4030086_saurabh');
	define('PASSWORD','sunshine');
	define('DB','id4030086_account');
	
	$con = mysqli_connect(HOST,USER,PASSWORD,DB) or die;
	
	
	$main_name = $_POST['username'];
	$password = $_POST['password'];
	
	$find_sql = "select name from account";
	$result = mysqli_query($con , $find_sql);
	$is=0;
	while($row = mysqli_fetch_assoc($result)){
		if($row['name'] == $main_name){
			$is = 1;
		}
	}
	
	if($is == 0){
	    $sql = "insert into account (name , Password) values ('$main_name' , '$password')";
	    mysqli_query($con , $sql);
	    echo "yes";
	}else{
	    echo "no";
	}
?>