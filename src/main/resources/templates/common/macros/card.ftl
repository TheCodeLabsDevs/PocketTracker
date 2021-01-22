<#macro card>
    <div class="card">
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
        <#if title?has_content><h5 class="card-title">${title}</h5></#if>
        <#if message?has_content><p class="card-text">${message}</p></#if>
        <#nested>
    </div>
</#macro>