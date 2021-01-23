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

<#macro h4 title>
    <div class="row">
        <div class="col-md-12 d-flex">
            <h4>${title}</h4>
            <#nested>
        </div>
    </div>
</#macro>

<#macro back_button label="Zurück" icon="" classes="">
    <a class="btn btn-secondary ${classes}" onclick="window.history.back()" role="button">
        <#if icon?has_content><i class="${icon}"></i></#if>
        ${label}
    </a>
</#macro>

<#macro button label url icon="" classes="" id="">
    <a class="btn btn-primary ${classes}" id="${id}" href="<@s.url url/>" role="button">
        <#if icon?has_content><i class="${icon}"></i></#if>
        ${label}
    </a>
</#macro>

<#macro row>
    <div class="row">
        <#nested>
    </div>
</#macro>

<#macro col size="col-12">
    <div class="${size}">
        <#nested>
    </div>
</#macro>