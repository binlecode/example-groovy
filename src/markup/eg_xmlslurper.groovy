/**
 * a good simple explanation comparing slurper and parser is here:
 * https://www.oodlestechnologies.com/blogs/XMLParser-and-XMLSlurper
 *
 */

def response = '''
<MT1_Item itemId="2404" itemType="MDI_1.1" itemName="title">
  <ItemBlob>
    <MDI>
      <Metadata>
        <AMS Asset_Class="package" Asset_ID="SUND0000000000001234" />
        <App_Data App="MOD" Name="Metadata_Spec_Version" Value="CableLabsVOD1.1"/>
        <App_Data App="MOD" Name="Provider_Content_Tier" Value="CABLE_EXCLUSIVE_12"/>
      </Metadata>
      <Asset>
        <Metadata>
          <AMS Asset_Class="title" Asset_ID="SUN" Asset_Name="Closer_title" Creation_Date="2010-02-02" />
        </Metadata>
        <Asset>
          <Metadata>
          </Metadata>
          <Content Value="poster.bmp"/>
        </Asset>
      </Asset>
    </MDI>
  </ItemBlob>
</MT1_Item>
'''


def flag = 'off'
response.eachLine { line ->
    if (line.contains('<MT1_Item') && flag.equals('off')) {
        flag = 'on'
        println '----> here starts a new mt1 xml: \n'
    }

    if (flag.equals('on')) {
        println line
    }

    if (line.contains('</MT1_Item') && flag.equals('on')) {
        flag = 'off'
        println '---------> here ends a mt1 xml \n\n'
    }
}


def mt1Item = new XmlSlurper().parseText(response)
println mt1Item.ItemBlob.size()
println mt1Item.@itemId.text()


