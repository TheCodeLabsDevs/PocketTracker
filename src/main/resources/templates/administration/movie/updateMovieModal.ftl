<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/form.ftl" as f/>


<@m.modal id="updateMovieFromApiModal" modalSize="modal-lg">
    <div class="modal-header">
        <h5 class="modal-title"><@s.messageArgs code="movie.api.update" args=[movie.getName()]/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    </div>
    <@m.body>
        <#list apiTypes as apiType>
            <@b.h3 title=apiType>
                <@f.form name="formUpdateMovieFromApi" url="/movie/${movie.id}/updateFromApi" classes="ms-5">
                    <@b.row>
                        <@f.hidden id="" name="apiType" value=apiType/>
                        <@f.submit label="button.update.movie.specific"/>
                    </@b.row>
                </@f.form>
            </@b.h3>
            <hr/>
        </#list>
    </@m.body>
    <@m.footer>
        <@m.cancelButton/>
    </@m.footer>
</@m.modal>

