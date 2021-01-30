<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/users/form.ftl" as form>

    <@template.head currentPage/>
    <@template.body>
        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="card-title text-center mb-2 text-truncate">${currentPage}</h2>

                <div class="row mt-3 mt-md-5">
                    <div class="col-12 mt-3 mt-md-0">
                        <div class="row">
                            <#list statisticItems as item>
                                <@showMacros.factItem icon=item.getIcon() value="" description=item.getText()/>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </@template.body>
</html>

