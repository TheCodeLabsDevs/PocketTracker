<#import "/common/components/base.ftl" as b/>

<#macro card margin="">
    <div class="card shadow-sm ${margin}">
        <#nested>
    </div>
</#macro>

<#macro header title>
    <div class="card-header">
        ${title}
    </div>
</#macro>

<#macro body title="" message="">
    <div class="card-body">
        <#if title?has_content><h5 class="card-title"><@b.localize title/></h5></#if>
        <#if message?has_content><p class="card-text"><@b.localize message/></p></#if>
        <#nested>
    </div>
</#macro>