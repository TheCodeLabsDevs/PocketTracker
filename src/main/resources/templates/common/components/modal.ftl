<#import "/common/components/base.ftl" as b/>

<#macro open label modalId style="btn-primary" classes="">
    <button type="button" class="btn btn-sm ${style} ${classes}" data-bs-toggle="modal" data-bs-target="#${modalId}">
        <@b.localize label/>
    </button>
</#macro>

<#macro modal id>
    <div class="modal fade" id="${id}" tabindex="-1">
        <div class="modal-dialog">
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