import React from 'react'
import PropTypes from 'prop-types'
import { Form, Row, Col, Cascader, Input } from 'antd'
import { address01, arrayToTree } from 'utils'

const FormItem = Form.Item

class AreaSelect extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      options: [],
      outCode: '',
    }
  }
  componentDidMount () {
    this.init()
  }
  componentWillReceiveProps () {
    this.reInit()
  }
  onChange (value, selectedOptions) {
    const targetCode = selectedOptions[selectedOptions.length - 1]
    if (targetCode) {
      this.setState({
        outCode: targetCode.outCode,
      })
    }
  }
  reInit () {
    const value = this.props.form.getFieldsValue([this.props.province.key])
    if (!value[this.props.province.key]) {
      this.setState({
        outCode: '',
      })
    }
  }
  init () {
    const addressData = []
    for (let item of address01) {
      if (item.id !== 0) {
        item.value = item.id
        item.label = item.areaName
        addressData.push(item)
      }
    }
    const data = arrayToTree(addressData, 'id', 'parentId', 'children')
    this.setState({
      options: data,
    })
  }
  render () {
    const { province, form: { getFieldDecorator } } = this.props
    return (
      <div>
        <Row>
          <Col span={24}>
            <FormItem label={province.label}>
              {getFieldDecorator(province.key, {
                rules: [
                  {
                    required: province.required,
                    message: '请选择区域地址',
                  },
                ],
              })(
                <Cascader
                  options={this.state.options}
                  onChange={this.onChange.bind(this)}
                  changeOnSelect
                  onBlur={this.props.TypeChange && this.props.TypeChange}
                  {...this.props}
                />
              )}
            </FormItem>
          </Col>
          <Col>
            <FormItem>
              {getFieldDecorator(province.outKey, {
                initialValue: this.state.outCode,
              })(
                <Input type="hidden" />
              )}
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
}

AreaSelect.propTypes = {
  province: PropTypes.object,
  form: PropTypes.object,
  city: PropTypes.object,
  area: PropTypes.object,
  TypeChange: PropTypes.func,
}

export default AreaSelect
