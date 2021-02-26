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
                <h2>Statistiken</h2>
            </@b.col>
        </@b.row>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-3 justify-content-center">
            <#list statisticItems as item>
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

