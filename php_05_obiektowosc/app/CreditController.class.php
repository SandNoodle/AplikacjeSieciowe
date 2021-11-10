<?php

// Wyłącz raportowanie błędów
error_reporting(0);

require_once $conf->root_path.'/lib/smarty/Smarty.class.php';
require_once $conf->root_path.'/lib/Messages.class.php';
require_once $conf->root_path.'/app/CreditForm.class.php';
require_once $conf->root_path.'/app/CreditResult.class.php';

class CreditController {
	private $msgs;
	private $page_form;
	private $result;
	private $hide_intro;
	
	public function __construct() {
		$this->msgs = new Messages();
		$this->credit_form = new CreditForm();
		$this->credit_result = new CreditResult();
		$this->hide_intro = false;
	}

	public function getParams() {
		$this->credit_form->amount = isset($_REQUEST ['amount']) ? $_REQUEST ['amount'] : null;
		$this->credit_form->years = isset($_REQUEST ['years']) ? $_REQUEST ['years'] : null;
		$this->credit_form->intrest = isset($_REQUEST ['intrest']) ? $_REQUEST ['intrest'] : null;
	}

	public function validateParams() {
		// Sprawdź, czy przekazano wszystkie argumenty
		if(!(isset($this->credit_form->amount)	&& isset($this->credit_form->years) && isset($this->credit_form->intrest))) {
			$this->msgs->addError('Nie przekazano wszystkich argumentów!');
			return false;
		}
		
		$this->hide_intro = true; // Przyszły pola formularza, więc - schowaj wstęp
		
		// Sprawdzenie czy wysłano niepuste wartości
		if(!($this->credit_form->amount)) { $this->msgs->addError('BŁĄD: Niepoprawna wartość parametru Kwoty!'); }
		if(!($this->credit_form->years)) { $this->msgs->addError('BŁĄD: Niepoprawna wartość parametru Lat!'); }
		if(!($this->credit_form->intrest)) { $this->msgs->addError('BŁĄD: Niepoprawna wartość parametru Stopy!'); }
		
		// Jeżeli nie wystąpiły błedy - sprawdź czy podano wartości liczbowe
		if(! $this->msgs->isError()) {
			if(!is_numeric($this->credit_form->amount)) {
				$this->msgs->addError('BŁĄD: Parametr Kwoty nie jest liczbą całkowitą!');
			}	
			
			if(!is_numeric($this->credit_form->years)) {
				$this->msgs->addError('BŁĄD: Parametr Lat nie jest liczbą całkowitą!');
			}	
			
			if(!is_numeric($this->credit_form->intrest)) {
				$this->msgs->addError('BŁĄD: Parametr Stopy Oprocentowania nie jest liczbą całkowitą!');
			}	
		}

		return !($this->msgs->isError());
	}

	public function process() {
		$this->getParams();

		if($this->validateParams()) {
			$this->msgs->addInfo('Parametry poprawne.');

			$intrest_rate = $this->credit_form->intrest / (12 * 100); // Oprocentowanie w skali miesiąca (% -> float)
			$loan_in_months = $this->credit_form->years * 12; // Liczba miesięcy spłaty kredytu
			$val1 = $intrest_rate * pow((1 + $intrest_rate), $loan_in_months); // Licznik
			$val2 = pow((1 + $intrest_rate), $loan_in_months) - 1; // Mianownik
			$this->credit_result->result = $this->credit_form->amount * ($val1 / $val2); // Miesięczna rata

			$this->msgs->addInfo('Wykonano obliczenie raty miesięcznej.');
		}

		$this->generateView();
	}

	public function generateView() {
		global $conf;

		$smarty = new Smarty();
		$smarty->assign('conf', $conf);
		
		$smarty->assign('page_title','Przykład 05');
		$smarty->assign('page_description','Obiektowość. Funkcjonalność aplikacji zamknięta w metodach różnych obiektów. Pełen model MVC.');
		$smarty->assign('page_header','Obiekty w PHP');

		$smarty->assign('hide_intro', $this->hide_intro);

		$smarty->assign('msgs', $this->msgs);
		$smarty->assign('form', $this->credit_form);
		$smarty->assign('res', $this->credit_result);
		
		$smarty->display($conf->root_path.'/app/CreditView.html');
	}

}
