from lxml import etree

class XMLPackage(object):

    # isError indicates if value is an error or not
    @staticmethod
    def server_package(version, status, value, isError):
        # create XML
        root = etree.Element('request')

        version_elm = etree.Element('version')
        version_elm.text = str(version)
        root.append(version_elm)

        status_elm = etree.Element('status')
        status_elm.text = str(status)
        root.append(status_elm)

        if isError:
            message_elm = etree.Element('error')
        else:
            message_elm = etree.Element('value')
        message_elm.text = value
        root.append(message_elm)

        without_checksum = etree.tostring(root)

        # TODO: sha checksum. need sal's commit

        s = etree.tostring(root)

        return s
print XMLPackage.server_package(1.0, 99, "blah", False)
print XMLPackage.server_package(1.0, 99, "asdf", True)
