<#import "/common/components/form.ftl" as f>
<#import "/common/components/modal.ftl" as m>

<#import "/spring.ftl" as s>

<#macro modal season modalId>
    <@m.modal id=modalId>
        <@m.header "season.delete"/>
        <@m.body>
            <@s.messageArgs code="season.delete.message" args=[season.getName()]/>
        </@m.body>
        <@m.footer>
            <@m.cancelButton/>
            <@f.form name="deleteSeason-${season.id}" url="/season/${season.id}/delete">
                <@f.submit label="season.delete" col=false style="btn-danger"/>
            </@f.form>
        </@m.footer>
    </@m.modal>
</#macro>