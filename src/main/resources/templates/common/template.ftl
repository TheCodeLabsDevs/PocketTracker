<#macro head title>
    <#import "/spring.ftl" as s/>

    <#--FTL Settings-->
    <#setting number_format="computer">


    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="<@s.url "/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>" rel="stylesheet"/>
        <link href="<@s.url "/webjars/font-awesome/6.7.2/css/all.min.css"/>" rel="stylesheet"/>
        <link href="<@s.url "/css/main.css"/>" rel="stylesheet">

        <script src="<@s.url "/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js"/>"></script>
        <script src="<@s.url "/js/main.js"/>"></script>

        <link rel="apple-touch-icon" href="<@s.url "/touch_icon.png"/>">

        <#nested>

        <title>PocketTracker - ${title}</title>

        <#if _csrf??>
            <meta name="_csrf" content="${_csrf.token}"/>
            <meta name="_csrf_header" content="${_csrf.headerName}"/>
        </#if>
        <script>
            const baseUrl = '<@s.url "/"/>';
        </script>
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
            <@b.localize toast/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </#if>

    <#nested>
</#macro>