<#import "/spring.ftl" as s/>

<#macro h1 title>
    <h1>${title}</h1>
</#macro>

<#macro h2 title>
    <div class="row">
        <div class="col-md-12 d-flex">
            <h2>${title}</h2>
            <#nested>
        </div>
    </div>
</#macro>

<#macro h3 title>
    <div class="row">
        <div class="col-md-12 d-flex">
            <h3>${title}</h3>
            <#nested>
        </div>
    </div>
</#macro>

<#macro button label url icon="" classes="">
    <a class="btn btn-primary ${classes}" href="<@s.url url/>" role="button">
        <#if icon?has_content><i class="${icon}"></i></#if>
        ${label}
    </a>
</#macro>

<#macro row>
    <div class="row">

    </div>
</#macro>

<#macro col size>
    <div class="${size}">

    </div>
</#macro>