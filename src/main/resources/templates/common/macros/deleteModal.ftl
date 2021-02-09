<#import "/common/components/form.ftl" as f>
<#import "/common/components/modal.ftl" as m>

<#import "/spring.ftl" as s>

<#macro modal season modalId title url deleteButton="button.delete">
    <@m.modal id=modalId>
        <@m.header title/>
        <@m.body>
            <#nested>
        </@m.body>
        <@m.footer>
            <@m.cancelButton/>
            <@f.form name="${modalId}-form" url=url>
                <@f.submit label=deleteButton col=false style="btn-danger"/>
            </@f.form>
        </@m.footer>
    </@m.modal>
</#macro>