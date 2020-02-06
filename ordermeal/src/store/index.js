import Vue from 'vue';
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    quantity: 0,
    searchShow: '',
  },
  mutations: {
    setQuantity(state, val) {
      state.quantity = val
    },
    setSearchShow(state, val) {
      if(val == '') {
        state.searchShow = false
      }else {
        state.searchShow = val
      }
    },
  }
})
