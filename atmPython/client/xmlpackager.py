from lxml import etree

class XMLPackage(object):

    @staticmethod
    def client_package(version, opcode, arguments):
        # create XML
        root = etree.Element('request')

        version_elm = etree.Element('version')
        version_elm.text = str(version)
        root.append(version_elm)

        content_length_elm = etree.Element('content_length')
        content_length_elm.text = ''
        root.append(content_length_elm)

        opcode_elm = etree.Element('opcode')
        opcode_elm.text = str(opcode)
        root.append(opcode_elm)

        if len(arguments) > 0:
            arguments_elm = etree.Element('arguments')

        for arg in arguments:
            arg_elm = etree.Element('arg')
            arg_elm.text = str(arg)
            arguments_elm.append(arg_elm)

        root.append(arguments_elm)

        without_length = etree.tostring(root)

        ''' Find number of digits of string without content length, and then
            add the number of digits with content length
        '''
        content_length = len(without_length) + len(str(len(without_length)))
        content_length_elm.text = str(content_length)

        s = etree.tostring(root)

        return s
