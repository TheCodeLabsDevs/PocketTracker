<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/common/components/card.ftl" as c/>

    <@template.head currentPage/>
    <@template.body>
        <@b.row classes="mb-4">
            <@b.col size="col-sm-12 col-md-8 col-lg-6" classes="mx-auto text-center">
                <h2><@b.localize "menu.statistics"/></h2>
            </@b.col>
        </@b.row>

        <h3 class="text-center my-3"><@b.localize "statistics.general"/></h3>

        <#list statisticItemsGeneral as item>
            <#if item?index % 2 == 0>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 justify-content-center m-auto">
            </#if>

                <div class="col">
                    <@c.card classes=item.getBackgroundColor().getBackgroundColor() + " " + item.getTextColor().getTextColor()>
                        <@c.body>
                            <@showMacros.factItem item.getIcon() "" item.getText()/>
                        </@c.body>
                    </@c.card>
                </div>

            <#if item?index % 2 != 0>
                </div>
            </#if>
        </#list>

        <hr class="mt-5"/>

        <h3 class="text-center mt-4 mb-3"><@b.localize "statistics.timeBased"/></h3>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 justify-content-center m-auto">
            <#list statisticItemsWatchTime as item>
                <div class="col">
                    <@c.card classes=item.getBackgroundColor().getBackgroundColor() + " " + item.getTextColor().getTextColor()>
                        <@c.body>
                            <@showMacros.factItem item.getIcon() "" item.getText()/>
                        </@c.body>
                    </@c.card>
                </div>
            </#list>
        </div>
    </@template.body>
</html>

