const COOKIE = {
  setCookie: (key, value, time = 30) => {
    const exp = new Date()
    if (time) {
      exp.setTime(exp.getTime() + time * 60 * 1000)
      document.cookie = `${key}=${escape(value)};expires=${exp.toGMTString()}`
    } else {
      document.cookie = `${key}=${escape(value)}`
    }
  },
  getCookie: (key) => {
    const reg = new RegExp(`(^| )${key}=([^;]*)(;|$)`)
    const arr = document.cookie.match(reg) || []
    if (arr.length) {
      return unescape(arr[2])
    }
    return null
  },
  delCookie: (key) => {
    let cval = COOKIE.getCookie(key)
    if (cval !== null) {
      COOKIE.setCookie(key, cval, -1)
    }
  },
  clearCookie: () => {
    let keys = document.cookie.match(new RegExp(/[^ =;]+(?=\=)/g))
    if (keys) {
      for (let key of keys) {
        COOKIE.delCookie(key)
      }
    }
  },
  keepCookie: (key, time = 30) => {
    if (key) {
      let value = COOKIE.getCookie(key)
      COOKIE.setCookie(key, value, time)
    } else {
      let keys = document.cookie.match(new RegExp(/[^ =;]+(?=\=)/g))
      if (keys) {
        for (let _key of keys) {
          let value = COOKIE.getCookie(_key)
          COOKIE.setCookie(_key, value, time)
        }
      }
    }
  },
}

module.exports = COOKIE
