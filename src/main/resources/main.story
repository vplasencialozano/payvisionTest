

Scenario Outline: scenario description
Given an endpoint to retrieve transaction to test for <name> and <password>
And filters <filters> and values <values>
When call endpoint to retrieve the transactions
Then information is as expected

Examples:
| name           | password    | filters                     | values            |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,GBP,date   |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,JPY,date   |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,USD,date   |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,EUR,date   |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,GBP,-date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,JPY,-date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,USD,-date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | credit,EUR,-date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,GBP,date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,JPY,date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,USD,date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,EUR,date  |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,GBP,-date |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,JPY,-date |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,USD,-date |
| code-challenge | payvisioner | action,currencyCode,orderBy | payment,EUR,-date |