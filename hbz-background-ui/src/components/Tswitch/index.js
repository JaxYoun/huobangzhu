import React from 'react'
import PropTypes from 'prop-types'
import { Switch } from 'antd'
import { request } from 'utils'
class SwitchCom extends React.Component {
  constructor (props) {
    super(props)
    const { checked } = this.props
    this.state = { checked }
  }
  getDefaultProps () {
    return {
      checked: false,
      checkedUrl: '',
      uncheckedUrl: '',
      callBack: (res) => res,
      param: {},
    }
  }
  onChange = () => {
    const { param, checkedUrl, uncheckedUrl, callBack } = this.props
    const { checked: currentState } = this.state
    this.setState({ checked: !currentState })
    const oprateUrl = currentState ? uncheckedUrl : checkedUrl
    request(oprateUrl, { data: param, showMsg: false }).then((res) => {
      setTimeout(() => {
        console.log(!res.success)
        !res.success && this.setState({ checked: currentState })
        callBack(res)
      }, 100)
    })
  }
  render () {
    const { checked } = this.state
    const Sprops = {
      onChange: () => { this.onChange() },
      checked,
    }
    return (<Switch ref={e => (this.susSwitch = e)} {...this.props} {...Sprops} />)
  }
}

SwitchCom.propTypes = {
  param: PropTypes.object,
  uncheckedUrl: PropTypes.string,
  checkedUrl: PropTypes.string,
  checked: PropTypes.bool,
  callBack: PropTypes.func,
}
export default SwitchCom
