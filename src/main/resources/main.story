

Scenario Outline: action
Given an endpoint to retrieve transaction to test for <name> and <password>
And filters <filters> and values <values>
When call endpoint to retrieve the transactions
Then information is as expected

Examples:
| name           | password    | filters      | values  |
| code-challenge | payvisioner | action       | credit  |

| code-challenge | payvisioner | action       | credit  |
| code-challenge | payvisioner | action       | payment |
| code-challenge | payvisioner | currencyCode | USD     |
| code-challenge | payvisioner | currencyCode | EUR     |
| code-challenge | payvisioner | currencyCode | GBP     |
| code-challenge | payvisioner | currencyCode | JPY     |
| code-challenge | payvisioner | orderBy      | date    |
| code-challenge | payvisioner | orderBy      | -date   |