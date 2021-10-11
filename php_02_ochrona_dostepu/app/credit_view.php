<?php require_once dirname(__FILE__).'/../config.php';?>

<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pl" lang="pl">
<head>
<meta charset="utf-8" />
<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
<title>Kalkulator Kredytowy</title>
</head>
<body>

<div style="width:90%; margin: 2em auto;">
	<a href="<?php print(_APP_ROOT); ?>/app/calc.php" class="pure-button">Do kalkulatora</a>
	<a href="<?php print(_APP_ROOT); ?>/app/inna_chroniona.php" class="pure-button">kolejna chroniona strona</a>
	<a href="<?php print(_APP_ROOT); ?>/app/security/logout.php" class="pure-button pure-button-active">Wyloguj</a>
</div>

<div style="width:90%; margin: 2em auto;">

<form action="<?php print(_APP_URL);?>/app/credit.php" method="post" class="pure-form pure-form-stacked">
	<!-- Kwota -->
	<label for="amount_id">Kwota kredytu (w PLN): </label>
	<input id="amount_id" type="number" name="amount" step="0.01" value="<?php print($amount_var); ?>"></label>
	</br>
	
	<!-- Liczba lat -->
	<label for="years_id">Liczba lat: </label>
	<input id="years_id" type="number" name="years" value="<?php print($years_var); ?>"></label>
	</br>

	<!-- Stopa oprocentowania -->
	<label for="intrest_id">Stopa oprocentowania (w %): </label>
	<input id="intrest_id" type="number" name="intrest" step="0.01" value="<?php print($intrest_var); ?>"></label>
	</br>

	<!-- Wyslij -->
	<input type="submit" value="Oblicz" />
</form>

<?php // Wyświetl wszelakie błędy
if (isset($messages)) {
	if (count ( $messages ) > 0) {
		echo '<ol style="margin: 20px; padding: 10px 10px 10px 30px; border-radius: 5px; background-color: #f88; width:auto; display: inline-block;">';
		foreach ( $messages as $key => $msg ) {
			echo '<li>'.$msg.'</li>';
		}
		echo '</ol>';
	}
}
?>

<?php // Podaj ratę miesięczną
if (isset($result)){ ?>
<div style="margin: 20px; padding: 10px; border-radius: 5px; background-color: #ff0; width:300px;">
<?php echo 'Miesięczna rata: '.$result. ' PLN!'; ?>
</div>
<?php } ?>

</div>

</body>
</html>