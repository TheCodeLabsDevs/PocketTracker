<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/form.ftl" as f/>


<@m.modal id="updateSeasonFromApiModal" modalSize="modal-lg">
    <div class="modal-header">
        <h5 class="modal-title"><@s.messageArgs code="show.api.season.update" args=[season.getNumber()]/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    </div>
    <@m.body>
        <#list episodeInfoByApi as apiType, episodeInfoList>
            <@b.h3 title=apiType>
                <@f.form name="formUpdateSeasonFromApi" url="/season/${season.id}/updateFromApi" classes="ms-5">
                    <@b.row>
                        <@f.hidden id="" name="apiType" value=apiType/>
                        <@f.submit label="button.update.season" icon="fas fa-rotate"/>
                    </@b.row>
                </@f.form>
            </@b.h3>

            <@b.row classes="p-3">
                <#list episodeInfoList as episodeInfo>
                    <@b.row>
                        <@b.col size="col-10">
                            <@b.localize "episode"/> ${episodeInfo.number()}: ${episodeInfo.name()}
                        </@b.col>
                    </@b.row>
                </#list>
            </@b.row>

            <hr/>
        </#list>
    </@m.body>
    <@m.footer>
        <@m.cancelButton/>
    </@m.footer>
</@m.modal>

