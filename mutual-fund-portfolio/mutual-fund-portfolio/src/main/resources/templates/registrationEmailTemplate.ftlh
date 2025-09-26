<!doctype html>
<html lang="en">
   <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
      <meta http-equiv="X-UA-Compatible" content="ie=edge">
      <style>
         table, th, td {
         border: 1px solid white;
         border-color: #96D4D4;
         border-collapse: collapse;
         text-align:center;
         }
         h4.inline-heading {
         display:inline;	
         }
         .outerdiv{
         border: 2px solid white;
         margin:auto;
         margin: 5px 5px;
         }
         div,p{
         color: white;
         }
         body{
         display: flex;
         justify-content: left;
         align-items: left;
         }
         table{
         margin: 0px 0px 10px 115px;
         width:40%;
         }
      </style>
   </head>
   <body style="margin: 0; padding: 0; display: table; width: 100%; text-align: left; background-color: black; color:white"">
      <div style="display: table-cell; vertical-align: middle; " class="outerdiv">
         <div style="margin:12px 0px 0px 6px;  text-align:left !important;">
            Hello,
            <h4 class="inline-heading"> ${user.firstName} ${user.lastName}</h4>
         </div>
         <h4 style="margin:6px 0px 6px 6px; text-align:left !important;"> Congratulations,</h4>
         <div margin-left:30px>You have successfully registered to our Mutual Fund Portfolio Application.</div>
         <br/>
         <div>Your mutual fund portfolio ID is:<b style="color:yellow !important"> ${user.portfolioNumber}</b>
         </div>
         <p> Please use this as your username while logging into our Mutual Fund Portfolio website.</p>
         <br/>
         <table>
            <tr>
               <th>Portfolio ID</td>
            </tr>
            <tr>
               <td> ${user.portfolioNumber}</td>
            </tr>
         </table >
         <div style="color:red !important" > Please do not share your credentials...!</div>
         <br/>
         <div style="text-align:left !important; margin: 0px 0px 0px 6px;">Best Regards,
            <br/>
            Team MFP
            </td>
         </div>
      </div>
   </body>
</html>