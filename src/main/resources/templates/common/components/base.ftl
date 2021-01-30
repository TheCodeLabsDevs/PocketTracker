<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>

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

<#macro h2 title raw=false classes="">
    <div class="row">
        <div class="col-md-12 d-flex">
            <h2 class="${classes}"><#if raw>${title}<#else><@localize title/></#if></h2>
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

<#macro back_button label="button.back" icon="" classes="me-2" url="" showLabel=true showIcon=true margin="mb-4" center=false style="btn-secondary">
    <#if center><div class="mx-auto text-center"></#if>
    <a class="btn ${style} ${margin} ${classes}"
       <#if url?has_content>href="<@s.url url/>" <#else>onclick="window.history.back()"</#if> role="button">
        <#if showIcon><i class="<#if icon?has_content>${icon}<#else>fas fa-arrow-left</#if>"></i></#if>
        <#if showLabel><@localize label/></#if>
    </a>
    <#if center></div></#if>
</#macro>

<#macro button label url icon="" classes="" id="" style="btn-primary">
    <a class="btn ${style} ${classes}" id="${id}" href="<@s.url url/>" role="button">
        <#if icon?has_content><i class="${icon}"></i></#if>
        <@localize label/>
    </a>
</#macro>

<#macro flex>
    <div class="d-flex flex-row">
        <#nested>
    </div>
</#macro>

<#macro row classes="">
    <div class="row ${classes}">
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
