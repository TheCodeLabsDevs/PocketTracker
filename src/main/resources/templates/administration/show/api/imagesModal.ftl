<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/form.ftl" as f/>


<#macro imagesModal show id title imageType>
    <@m.modal id=id modalSize="modal-xl">
        <@m.header title/>
        <@m.body>
            <#list urlsByApi as apiType, imageUrls>
                <@b.h3 title=apiType/>

                <@b.row classes="p-3">
                    <#list imageUrls as imageUrl>
                        <@b.col size="col-3">
                            <img src="${imageUrl}" class="img-fluid m-3 image-selectable" alt="${imageUrl}"/>
                        </@b.col>
                    </#list>
                </@b.row>
            </#list>

            <@f.form name="imageFromApi" url="/show/${show.id}/edit/imageFromApi/${imageType}" classes="hidden">
                <@f.hidden id="url" value=""/>
            </@f.form>
        </@m.body>
        <@m.footer>
            <@m.cancelButton/>
        </@m.footer>
    </@m.modal>
</#macro>
