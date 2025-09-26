<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <style>
       th, td {
            border: 1px solid white;
            border-color: #96D4D4;
            border-collapse: collapse;
            text-align:center;
           
        
        }

        h4.inline-heading {
            display: inline;
        }

        .outerdiv {
            border: 2px solid white;
            margin: 5px 5px;
        }

        div, p {
            color: white;
        }

        body {
            display: flex;
            justify-content: left;
            align-items: left;
            
        }

        table {
        margin-left:40px;
        width: 65%;
        
        }
        
    </style>
</head>
<body style="margin: 0; padding: 0; display: table; width: 100%; text-align: left; background-color: black; color:white">
    <div style="display: table-cell; vertical-align: middle; " class="outerdiv">
        <div style="margin: 12px 0px 0px 6px; text-align: left !important;">
            Hello,
            <h4 class="inline-heading">${transaction.user.firstName} ${transaction.user.lastName}</h4>
        </div>
        <h4 style="margin: 6px 0px 6px 6px; text-align: left !important;"> Congratulations,</h4>
        <div style="margin-left: 30px;">You have successfully ${transaction.transactionType}ED Rs.<span style="color: yellow !important">${transaction.amount}</span> in our <span>${transaction.mutualFundScheme.schemeName}</span> Mutual Fund Scheme.</div>
        <p> Please Keep this email for your future reference</p>
        <table>
            <tr>
                <th>Mutual Fund Type</th>
                <th>Mutual Fund Scheme</th>
                <th>Invested Date</th>
                <th>Transaction Amount</th>
                <th>Current Net Asset Value</th>
                <th>Units</th> 
            </tr>
            <tr>
	            <td>${transaction.mutualFundScheme.mutualFund.type}</td>
	            <td>${transaction.mutualFundScheme.schemeName}</td>
	            <td>${transaction.date}</td>
	            <td>${transaction.amount}</td>
	            <td>${transaction.netAssetValue}</td>
	            <td>${transaction.navUnits}</td>
            </tr>
        </table >
        <br/>
        <div style="text-align: left !important; margin: 0px 0px 0px 6px;">Best Regards,
            <br/>
            Team MFP
        </div>
    </div>
</body>
</html>
