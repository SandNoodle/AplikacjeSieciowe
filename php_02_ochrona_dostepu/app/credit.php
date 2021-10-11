<?php // Kontroler kalkulatora oprocentowania
require_once dirname(__FILE__).'/../config.php';

// Sprawdź czy ktokolwiek jest zalogowany - jeśli nikt - zakończ funkcję
include _ROOT_PATH.'/app/security/check.php';

// Wyłącz raportowanie błędów
error_reporting(0);

// Pobranie parametrów
function getParams(&$amount_var, &$years_var, &$intrest_var) {
	$amount_var = isset($_REQUEST['amount']) ? $_REQUEST['amount'] : null;
	$years_var = isset($_REQUEST['years']) ? $_REQUEST['years'] : null;
	$intrest_var = isset($_REQUEST['intrest']) ? $_REQUEST['intrest'] : null;
}

// Walidacja parametrów
function validate(&$amount_var ,&$years_var ,&$intrest_var ,&$messages) {
	// 	Brak przekazanych parametrów
	if(!(isset($amount_var) && isset($years_var) && isset($intrest_var))) {
		return false; // Niezalogowano bądź nie wysłano informacji
	}

	// 	Sprawdzenie czy wysłano niepuste wartości
	if(!$amount_var) { $messages[] = "BŁĄD: Niepoprawna wartość parametru Kwoty!"; }
	if(!$years_var) { $messages[] = "BŁĄD: Niepoprawna wartość parametru Lat!"; }
	if(!$intrest_var) { $messages[] = "BŁĄD: Niepoprawna wartość parametru Stopy!"; }
	
	//	Sprawdzenie podania poszczególnych wartości
	if(!is_numeric($amount_var)) {
		$messages [] = 'BŁĄD: Parametr Kwoty nie jest liczbą całkowitą!';
	}	
	
	if(!is_numeric($years_var)) {
		$messages [] = 'BŁĄD: Parametr Lat nie jest liczbą całkowitą!';
	}	
	
	if(!is_numeric($intrest_var)) {
		$messages [] = 'BŁĄD: Parametr Stopy Oprocentowania nie jest liczbą całkowitą!';
	}	
	
	
	// Zwróć niepoprawne działanie w przypadku braku / niepoprawności parametrów
	if(count($messages) != 0) return false;

	// Walidacja powiodła się sukcesem
	return true;
}

function process(&$amount_var, &$years_var, &$intrest_var, &$messages, &$result) {
	global $role;

	// Oblicz stopę oprocentowania jeśli zalogowany jest administrator
	if($role == 'admin') {
		$intrest_rate = $intrest_var / (12 * 100); // Oprocentowanie w skali miesiąca (% -> float)
		$loan_in_months = $years_var * 12; // Liczba miesięcy spłaty kredytu
		$val1 = $intrest_rate * pow((1 + $intrest_rate), $loan_in_months); // Licznik
		$val2 = pow((1 + $intrest_rate), $loan_in_months) - 1; // Mianownik
		$result = $amount_var * ($val1 / $val2); // Miesięczna rata
	} else {
		// Jeśli użytkownikiem nie jest adminem
		// Zablokuj możliwość używania kalkulatora
		$messages[] = "Kalkulatora kredytowego może używać tylko administrator!";
	}
}

// Definicja zmiennych kontrolera
$amount_var = null;
$years_var = null;
$intrest_var = null;
$result = null;
$messages = array();

getParams($amount_var, $years_var, $intrest_var);
if(validate($amount_var, $years_var, $intrest_var, $messages)) {
	process($amount_var, $years_var, $intrest_var, $messages, $result);
}

// Wywołaj widok
include 'credit_view.php';