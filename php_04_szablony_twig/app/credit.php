<?php // Kontroler kalkulatora oprocentowania
require_once dirname(__FILE__).'/../config.php';

// Załaduj Twig
require_once _ROOT_PATH.'/lib/Twig/Autoloader.php';

// Wyłącz raportowanie błędów
// error_reporting(0);

// Definicja zmiennych kontrolera
$hide_intro = false;
$amount_var = null;
$years_var = null;
$intrest_var = null;
$result = null;
$messages = array();
$infos = array();

// Pobranie parametrów
function getParams(&$amount_var, &$years_var, &$intrest_var) {
	$amount_var = isset($_REQUEST['amount']) ? $_REQUEST['amount'] : null;
	$years_var = isset($_REQUEST['years']) ? $_REQUEST['years'] : null;
	$intrest_var = isset($_REQUEST['intrest']) ? $_REQUEST['intrest'] : null;
}

// Walidacja parametrów
function validate(&$amount_var ,&$years_var ,&$intrest_var ,&$messages, &$infos) {
	$hide_intro = true;
	// 	Brak przekazanych parametrów
	if(!(isset($amount_var) && isset($years_var) && isset($intrest_var))) {
		return false; // Niezalogowano bądź nie wysłano informacji
	}
	
	$infos [] = 'Przekazano parametry.';

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

function process(&$amount_var, &$years_var, &$intrest_var, &$messages, &$infos, &$result) {
	global $role;


	$infos [] = 'Parametry poprawne. Wykonuję obliczenia.';

	// Oblicz stopę oprocentowania jeśli zalogowany jest administrator
	$intrest_rate = $intrest_var / (12 * 100); // Oprocentowanie w skali miesiąca (% -> float)
	$loan_in_months = $years_var * 12; // Liczba miesięcy spłaty kredytu
	$val1 = $intrest_rate * pow((1 + $intrest_rate), $loan_in_months); // Licznik
	$val2 = pow((1 + $intrest_rate), $loan_in_months) - 1; // Mianownik
	$result = $amount_var * ($val1 / $val2); // Miesięczna rata
}

getParams($amount_var, $years_var, $intrest_var);
if(validate($amount_var, $years_var, $intrest_var, $messages, $infos)) {
	process($amount_var, $years_var, $intrest_var, $messages, $infos, $result);
}

//start Twig
Twig_Autoloader::register();
//załaduj szablony (wskazanie folderów z potrzebnymi szablonami)
$loader = new Twig_Loader_Filesystem(_ROOT_PATH.'/templates'); //szablon ogólny
$loader->addPath(_ROOT_PATH.'/app'); //szablon strony kalkulatora
//skonfiguruj folder cache
$twig = new Twig_Environment($loader, array(
    'cache' => _ROOT_PATH.'/twig_cache',));

// Przygotowanie zmiennych dla szablonu
$variables = array(
	'app_url' => _APP_URL,
	'root_path' => _ROOT_PATH,
	'page_title' => 'Przykład 04',
	'page_description' => 'Profesjonalne szablonowanie oparte na bibliotece Twig',
	'page_header' => 'Szablony Twig',
	'hide_intro' => $hide_intro
);

if (isset($amount_var)) $variables ['amount_id'] = $amount_var;
if (isset($years_var)) $variables ['years_id'] = $years_var;
if (isset($intrest_var)) $variables ['intrest_id'] = $intrest_var;
if (isset($result)) $variables ['result'] = $result;
if (isset($messages)) $variables ['messages'] = $messages;
if (isset($infos)) $variables ['infos'] = $infos;

// Wywołaj widok
echo $twig->render('credit.html', $variables);
