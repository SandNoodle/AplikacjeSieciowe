<?php
// KONTROLER strony kalkulatora
require_once dirname(__FILE__).'/../config.php';

//załaduj Twig
require_once _ROOT_PATH.'/lib/Twig/Autoloader.php';

// Wyłącz raportowanie o błędach
// error_reporting(0);

// W kontrolerze niczego nie wysyła się do klienta.
// Wysłaniem odpowiedzi zajmie się odpowiedni widok.
// Parametry do widoku przekazujemy przez zmienne.

// 1. pobranie parametrów

$x = isset($_REQUEST['x']) ? $_REQUEST['x'] : '';
$y = isset($_REQUEST['y']) ? $_REQUEST['y'] : '';
$operation = isset($_REQUEST['op']) ? $_REQUEST['op'] : '';

// 2. walidacja parametrów z przygotowaniem zmiennych dla widoku

//domyślnie pokazuj wstęp strony (tytuł i tło)
$hide_intro = false;

// FIX: Bez tej linii PHP 8.0 narzeka na podawaną wartość NULL.
$messages = array();

// sprawdzenie, czy parametry zostały przekazane - jeśli nie to wyświetl widok bez obliczeń
if ( isset($_REQUEST['x']) && isset($_REQUEST['y']) && isset($_REQUEST['op']) ) {
	//nie pokazuj wstępu strony gdy tryb obliczeń (aby nie trzeba było przesuwać)
	$hide_intro = true;

	$infos [] = 'Przekazano parametry.';

	// sprawdzenie, czy potrzebne wartości zostały przekazane
	if ( $x == "") {
		$messages [] = 'Nie podano liczby 1';
	}
	if ( $y == "") {
		$messages [] = 'Nie podano liczby 2';
	}
	
	//nie ma sensu walidować dalej gdy brak parametrów
	if (count ( $messages ) == 0) {
	
		// sprawdzenie, czy $x i $y są liczbami całkowitymi
		if (! is_numeric( $x )) {
			$messages [] = 'Pierwsza wartość nie jest liczbą';
		}
	
		if (! is_numeric( $y )) {
			$messages [] = 'Druga wartość nie jest liczbą';
		}
	
	}
	
	// 3. wykonaj zadanie jeśli wszystko w porządku
	
	if (count ( $messages ) == 0) { // gdy brak błędów
		
		$infos [] = 'Parametry poprawne. Wykonuję obliczenia.';
		
		//konwersja parametrów na int
		$x = floatval($x);
		$y = floatval($y);
	
		//wykonanie operacji
		switch ($operation) {
		case 'minus' :
			$result = $x - $y;
			$operation_name = '-';
			break;
		case 'times' :
			$result = $x * $y;
			$operation_name = '*';
			break;
		case 'div' :
			$result = $x / $y;
			$operation_name = '/';
			break;
		default :
			$result = $x + $y;
			$operation_name = '+';
			break;
		}
	}
}

// 4. Przygotowanie szablonu i zmiennych

//start Twig
Twig_Autoloader::register();
//załaduj szablony (wskazanie folderów z potrzebnymi szablonami)
$loader = new Twig_Loader_Filesystem(_ROOT_PATH.'/templates'); //szablon ogólny
$loader->addPath(_ROOT_PATH.'/app'); //szablon strony kalkulatora
//skonfiguruj folder cache
$twig = new Twig_Environment($loader, array(
    'cache' => _ROOT_PATH.'/twig_cache',
));

//przygotowanie zmiennych dla szablonu
$variables = array(
	'app_url' => _APP_URL,
	'root_path' => _ROOT_PATH,
	'page_title' => 'Przykład 04',
	'page_description' => 'Profesjonalne szablonowanie oparte na bibliotece Twig',
	'page_header' => 'Szablony Twig',
	'hide_intro' => $hide_intro
);
if (isset($x)) $variables ['x'] =  $x;
if (isset($y)) $variables ['y'] = $y;
if (isset($result)) $variables ['result'] = $result;
if (isset($messages)) $variables ['messages'] = $messages;
if (isset($infos)) $variables ['infos'] = $infos;
if (isset($operation)) $variables ['operation'] = $operation;
if (isset($operation_name)) $variables ['operation_name'] = $operation_name;

// 5. Wywołanie szablonu (wygenerowanie widoku)
echo $twig->render('calc.html', $variables);
