<#import "/spring.ftl" as s/>

<#macro hasPermission permission>
    <#if currentUser?? && currentUser.userRole.name() == permission>
        <#nested>
    </#if>
</#macro>

<#macro localize label>
    <#attempt>
        <@s.message label/>
        <#recover>
            ${label}
    </#attempt>
</#macro>

<#macro h1 title raw=false>
    <h1><#if raw>${title}<#else><@localize title/></#if></h1>
</#macro>

<#macro h2 title raw=false>
    <div class="row">
        <div class="col-md-12 d-flex">
            <h2><#if raw>${title}<#else><@localize title/></#if></h2>
            <#nested>
        </div>
    </div>
</#macro>

<#macro h3 title raw=false>
    <div class="row">
        <div class="col-md-12 d-flex">
            <h3><#if raw>${title}<#else><@localize title/></#if></h3>
            <#nested>
        </div>
    </div>
</#macro>

<#macro h4 title raw=false>
    <div class="row">
        <div class="col-md-12 d-flex">
            <h4><#if raw>${title}<#else><@localize title/></#if></h4>
            <#nested>
        </div>
    </div>
</#macro>

<#macro back_button label="button.back" icon="" classes="" showIcon=true margin="mb-4" center=false>
    <div class="<#if center>mx-auto text-center</#if>">
        <a class="btn btn-primary ${margin} ${classes}" onclick="window.history.back()" role="button">
            <#if showIcon><i class="<#if icon?has_content>${icon}<#else>fas fa-arrow-left</#if>"></i></#if>
            <@localize label/>
        </a>
    </div>
</#macro>

<#macro button label url icon="" classes="" id="" style="btn-primary">
    <a class="btn ${style} ${classes}" id="${id}" href="<@s.url url/>" role="button">
        <#if icon?has_content><i class="${icon}"></i></#if>
        <@localize label/>
    </a>
</#macro>

<#macro row>
    <div class="row">
        <#nested>
    </div>
</#macro>

<#macro col size="col-12" classes="">
    <div class="${size} ${classes}">
        <#nested>
    </div>
</#macro>

<#macro separator>
    <@col size="col-12">
        <hr>
    </@col>
</#macro>
