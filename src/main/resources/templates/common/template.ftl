<#macro head title>
    <#import "/spring.ftl" as s/>

    <#--FTL Settings-->
    <#setting number_format="computer">


    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="<@s.url "/webjars/bootstrap/5.0.0-beta1/css/bootstrap.min.css"/>" rel="stylesheet"/>
        <link href="<@s.url "/webjars/font-awesome/5.15.2/css/all.min.css"/>" rel="stylesheet"/>
        <link href="<@s.url "/css/main.css"/>" rel="stylesheet">

        <script src="<@s.url "/webjars/bootstrap/5.0.0-beta1/js/bootstrap.bundle.min.js"/>"></script>
        <script src="<@s.url "/js/main.js"/>"></script>

        <#nested>

        <title>PocketTracker - ${title}</title>
    </head>
</#macro>

<#macro body container=true>
    <body class="bg-light">

        <#import "/common/navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <#if container>
                <div class="py-5">
                    <div class="container">
                        <@content>
                            <#nested/>
                        </@content>
                    </div>
                </div>
            <#else>
                <@content>
                    <#nested/>
                </@content>
            </#if>
        </main>
    </body>
</#macro>

<#macro content>
    <#import "/common/components/base.ftl" as b>

    <#if toast??>
        <div class="alert ${toast.getColor().getToastColor()} alert-dismissible fade show" role="alert">
            <@b.localize toast.getMessage()/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </#if>

    <#nested>
</#macro>