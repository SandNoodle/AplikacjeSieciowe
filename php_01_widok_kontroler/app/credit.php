<?php // Kontroler kalkulatora oprocentowania
require_once dirname(__FILE__).'/../config.php';

// Wyłącz raportowanie błędów
error_reporting(0);

// Pobranie parametrów
$amount_var = $_REQUEST['amount'];
$years_var = $_REQUEST['years'];
$intrest_var = $_REQUEST['intrest'];

// Walidacja parametrów
// 	Brak przekazanych parametrów
if(!(isset($amount_var) && isset($years_var) && isset($intrest_var))) {
	$messages [] = 'BŁĄD: Brak któregoś z parametrów!';
}

//	Sprawdzenie podania poszczególnych wartości
// Kwota
if(!$amount_var) {
	$messages [] = 'BŁĄD: Brak parametru Kwoty!';
} else {
	if(!is_numeric($amount_var)) {
		$messages [] = 'BŁĄD: Parametr Kwoty nie jest liczbą całkowitą!';
	}
}

// Lata
if(!$years_var) {
	$messages [] = 'BŁĄD: Brak parametru Lat!';	
} else {
	if(!is_numeric($years_var)) {
		$messages [] = 'BŁĄD: Parametr Lat nie jest liczbą całkowitą!';
	}
}

// Stopa
if(!$intrest_var) {
	$messages [] = 'BŁĄD: Brak parametru Stopy Oprocentowania!';
} else {
	if(!is_numeric($intrest_var)) {
		$messages [] = 'BŁĄD: Parametr Stopy Oprocentowania nie jest liczbą całkowitą!';
	}
}

// W przypadku braku błędów wykonaj program
if(empty($messages)) {
	// Oblicz stopę oprocentowania
	$intrest_rate = $intrest_var / (12 * 100); // Oprocentowanie w skali miesiąca (% -> float)
	$loan_in_months = $years_var * 12; // Liczba miesięcy spłaty kredytu
	$val1 = $intrest_rate * pow((1 + $intrest_rate), $loan_in_months); // Licznik
	$val2 = pow((1 + $intrest_rate), $loan_in_months) - 1; // Mianownik
	$result = $amount_var * ($val1 / $val2); // Miesięczna rata
}

// Wywołaj widok
include 'credit_view.php';