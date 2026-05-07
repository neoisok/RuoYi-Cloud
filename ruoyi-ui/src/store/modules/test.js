import Cookies from "js-cookie";

const state = {
    sidebar:{
        opened:Cookies.get('sidebarStatus')?!!+Cookies.get('sidebarStatus'):true,
        withoutAnimation:false,
        hide:false
    },
    device:'desktop',
    size: Cookies.get('size')||'medium'
}

const mutations = {
    TOGGLE_SIDEBAR:state => {
        if(state.sidebar.hide){
            return false;
        }
        state.sidebar.opened = !state.sidebar.opened
        state.sidebar.withoutAnimation = false 
        if(state.sidebar.opened){
            Cookies.set('sidebarStatus',1)
        }else{
            Cookies.set('sidebarStatus',0)
        }
    }
}