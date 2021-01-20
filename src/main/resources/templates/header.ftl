<#macro header title>
    <#import "spring.ftl" as s/>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="<@s.url "/webjars/bootstrap/5.0.0-beta1/css/bootstrap.min.css"/>" rel="stylesheet"/>
    <link href="<@s.url "/webjars/font-awesome/5.15.2/css/all.min.css"/>" rel="stylesheet"/>
    <link href="<@s.url "/css/main.css"/>" rel="stylesheet">

    <script src="<@s.url "/webjars/bootstrap/5.0.0-beta1/js/bootstrap.bundle.min.js"/>"></script>

    <title>PocketTracker - ${title}</title>
</#macro>