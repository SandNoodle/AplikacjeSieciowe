<?php

// Skrypt uruchamiający akcję wykonania obliczeń kalkulatora

require_once dirname(__FILE__).'./../config.php';

// Załaduj kontroler
require_once $conf->root_path.'/app/CreditController.class.php';

// Utwórz i użyj obiekt
$credit_controller = new CreditController();
$credit_controller->process();
