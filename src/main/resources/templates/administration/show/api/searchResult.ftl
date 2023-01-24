<#import "/common/components/base.ftl" as b>
<#import "/common/components/form.ftl" as f>

<table class="table" id="searchResultTable">
    <tr>
        <th><@b.localize "show.apiIdentifiers.search.name"/></th>
        <th><@b.localize "show.apiIdentifiers.search.firstAired"/></th>
        <th><@b.localize "show.apiIdentifiers.search.identifier"/></th>
        <th></th>
    </tr>
    <#list items as item>
        <tr>
            <td>${item.name}</td>
            <td>${item.firstAired}</td>
            <td>${item.identifier?c}</td>
            <td>
                <@f.form name="search-${item.identifier?c}" url="/show/createFromApi">
                    <@f.hidden id="type" value="${type}"/>
                    <@f.hidden id="identifier" value="${item.identifier?c}"/>
                    <@f.submit label="button.add" form="search-${item.identifier?c}" col=false/>
                </@f.form>
            </td>
        </tr>
    </#list>
</table>