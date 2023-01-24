<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/modal.ftl" as m/>


<@m.modal id="addPoster" modalSize="modal-xl">
    <@m.header "show.poster.fromApi"/>
    <@m.body>
        <#list posterUrlsByApi as apiType, imageUrls>
            <@b.h3 title=apiType/>

            <@b.row classes="p-3">
                <#list imageUrls as imageUrl>
                    <@b.col size="col-3">
                        <img src="${imageUrl}" class="img-fluid m-3"/>
                    </@b.col>
                </#list>
            </@b.row>
        </#list>
    </@m.body>
    <@m.footer>
        <@m.cancelButton/>
    </@m.footer>
</@m.modal>
