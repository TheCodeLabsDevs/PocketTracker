<#import "/common/components/base.ftl" as b/>

<#macro open label modalId style="btn-primary" classes="mb-4" buttonSize="btn-sm" icon="">
    <button type="button" class="btn ${buttonSize} ${style} ${classes}" data-bs-toggle="modal" data-bs-target="#${modalId}">
        <#if icon?has_content><i class="${icon}"></i></#if>
        <@b.localize label/>
    </button>
</#macro>

<#macro openIcon icon modalId classes="" iconStyle="">
    <a data-bs-toggle="modal" data-bs-target="#${modalId}" class="px-2 table-action ${classes}">
        <i class="${icon} ${iconStyle}"></i>
    </a>
</#macro>

<#macro cancelButton label="button.cancel" style="btn-secondary">
    <button type="button" class="btn ${style}" data-bs-dismiss="modal"><i class="fas fa-times"></i> <@b.localize label/></button>
</#macro>

<#macro modal id center=true modalSize="">
    <div class="modal fade" id="${id}" tabindex="-1">
        <div class="modal-dialog <#if center>modal-dialog-centered modal-dialog-scrollable</#if> ${modalSize}">
            <div class="modal-content">
                <#nested>
            </div>
        </div>
    </div>
</#macro>

<#macro header title>
    <div class="modal-header">
        <h5 class="modal-title"><@b.localize title/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    </div>
</#macro>

<#macro body>
    <div class="modal-body">
        <#nested>
    </div>
</#macro>

<#macro footer>
    <div class="modal-footer">
        <#nested>
    </div>
</#macro>