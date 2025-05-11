<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/form.ftl" as f/>


<@m.modal id="importSeasonFromApiModal" modalSize="modal-lg">
    <@m.header "show.api.season.create"/>
    <@m.body>
        <#list seasonInfoByApi as apiType, seasonInfoList>
            <@b.h3 title=apiType/>

            <@b.row classes="p-3">
                <#list seasonInfoList as seasonInfo>
                    <@f.form name="formAddSeasonByApi" url="/show/${show.id}/season/addFromApi">
                        <@b.row>
                            <@b.col size="col-9">
                                <@b.localize "season"/> ${seasonInfo.name()} (${seasonInfo.numberOfEpisodes()} <@b.localize "season.episodes"/>)
                            </@b.col>
                            <@f.hidden id="" name="apiType" value=apiType/>
                            <@f.hidden id="" name="seasonId" value=seasonInfo.id()/>
                            <@f.submit size="col-3" label="button.add" icon="fas fa-add"/>
                        </@b.row>
                    </@f.form>
                </#list>
            </@b.row>

            <hr/>
        </#list>
    </@m.body>
    <@m.footer>
        <@m.cancelButton/>
    </@m.footer>
</@m.modal>

